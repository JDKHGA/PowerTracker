import { serve } from "https://deno.land/std@0.168.0/http/server.ts"
import { createClient } from 'https://esm.sh/@supabase/supabase-js@2'

serve(async (req) => {
  try {
    // 1. Extract meterId and balanceKwh from the request body
    const { meterId, balanceKwh } = await req.json()

    const supabase = createClient(
      Deno.env.get('SUPABASE_URL') ?? '',
      Deno.env.get('SUPABASE_SERVICE_ROLE_KEY') ?? ''
    )

    // 2. Fetch usage logs for the last 7 days to summarize
    const sevenDaysAgo = new Date()
    sevenDaysAgo.setDate(sevenDaysAgo.getDate() - 7)
    
    const { data: rawLogs } = await supabase
      .from('usage_logs')
      .select('usage_kwh, logged_at')
      .eq('meter_id', meterId)
      .gte('logged_at', sevenDaysAgo.toISOString())
      .order('logged_at', { ascending: true })

    // Summarize logs by day
    const dailySummaries: Record<string, number> = {}
    rawLogs?.forEach(log => {
      const date = log.logged_at.split('T')[0]
      dailySummaries[date] = (dailySummaries[date] || 0) + log.usage_kwh
    })
    
    const summarizedLogs = Object.entries(dailySummaries).map(([date, totalUsage]) => ({
      date,
      totalUsageKwh: parseFloat(totalUsage.toFixed(2))
    }))

    const GEMINI_API_KEY = Deno.env.get('GEMINI_API_KEY')
    if (!GEMINI_API_KEY) throw new Error("GEMINI_API_KEY secret is missing!")

    // 3. Call Gemini with summarized data
    const MODEL_ID = "gemini-3-flash-preview" // Recommended faster model
    const response = await fetch(`https://generativelanguage.googleapis.com/v1beta/models/${MODEL_ID}:generateContent?key=${GEMINI_API_KEY}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        contents: [
          {
            role: "user",
            parts: [
              {
                text: `You are an energy expert analyzer.
                      Current Meter Balance: ${balanceKwh} kWh.
                      Daily Usage Summary (last 7 days): ${JSON.stringify(summarizedLogs)}.

                      Tasks:
                      1. Calculate the daily burn rate based on the summarized daily totals.
                      2. Using the current balance of ${balanceKwh} kWh, predict exactly when it will hit zero.
                      3. Write a human-like forecast sentence (e.g., "At your current rate of 2.1 kWh/day, your credit is expected to last until Friday evening, Dec 27").
                      4. Provide 3 specific, actionable saving tips based on the observed daily usage patterns.

                      Return ONLY a raw JSON object with this exact structure:
                      {"forecastDate": "string", "peakUsageDescription": "string", "suggestions": ["string"]}`
                }
            ]
          }
        ],
        generationConfig: {
          temperature: 0.7
        }
      })
    })

    const result = await response.json()

    if (result.error) {
      return new Response(JSON.stringify({
        error: "Gemini API Error",
        details: result.error.message
      }), { status: 500, headers: { "Content-Type": "application/json" } })
    }

    if (!result.candidates || !result.candidates[0]) {
      return new Response(JSON.stringify({
        error: "Gemini Error",
        details: "No response generated"
      }), { status: 500, headers: { "Content-Type": "application/json" } })
    }

    let aiText = result.candidates[0].content.parts[0].text
    // Strip markdown JSON blocks if AI includes them
    aiText = aiText.replace(/```json|```/g, "").trim()

    return new Response(aiText, { headers: { "Content-Type": "application/json" } })

  } catch (error) {
    return new Response(JSON.stringify({ error: error.message }), {
      status: 500,
      headers: { "Content-Type": "application/json" }
    })
  }
})
