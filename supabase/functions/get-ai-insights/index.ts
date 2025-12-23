import { serve } from "https://deno.land/std@0.168.0/http/server.ts"
import { createClient } from 'https://esm.sh/@supabase/supabase-js@2'

serve(async (req) => {
  try {
    const { meterId } = await req.json()

    const supabase = createClient(
      Deno.env.get('SUPABASE_URL') ?? '',
      Deno.env.get('SUPABASE_SERVICE_ROLE_KEY') ?? ''
    )

    // 1. Fetch historical data
    const { data: logs } = await supabase
      .from('usage_logs')
      .select('usage_kwh, logged_at')
      .eq('meter_id', meterId)
      .limit(50)

    const GEMINI_API_KEY = Deno.env.get('GEMINI_API_KEY')
    if (!GEMINI_API_KEY) throw new Error("GEMINI_API_KEY secret is missing!")

    // 2. Call Gemini
    const MODEL_ID = "gemini-3-flash-preview"
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
                text: `You are an energy expert analyzer. Analyze these power logs: ${JSON.stringify(logs)}.
                Based on these logs, provide:
                1. A descriptive forecast sentence estimating when credit will run out (e.g., "Based on your patterns, your credit is expected to last until Dec 28, 2024").
                2. A description of the user's peak usage patterns.
                3. Three actionable savings tips.

                Return ONLY a JSON object with this exact structure:
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
    aiText = aiText.replace(/```json|```/g, "").trim()

    return new Response(aiText, { headers: { "Content-Type": "application/json" } })

  } catch (error) {
    return new Response(JSON.stringify({ error: error.message }), {
      status: 500,
      headers: { "Content-Type": "application/json" }
    })
  }
})
