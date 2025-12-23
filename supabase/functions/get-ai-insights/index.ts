import { serve } from "https://deno.land/std@0.168.0/http/server.ts"
import { createClient } from 'https://esm.sh/@supabase/supabase-js@2'

serve(async (req) => {
  try {
    const { meterId } = await req.json()

    // 1. Setup Supabase Client
    const supabase = createClient(
      Deno.env.get('SUPABASE_URL') ?? '',
      Deno.env.get('SUPABASE_SERVICE_ROLE_KEY') ?? ''
    )

    // 2. Fetch logs
    const { data: logs } = await supabase
      .from('usage_logs')
      .select('usage_kwh, logged_at')
      .eq('meter_id', meterId)
      .order('logged_at', { ascending: false })
      .limit(100)

    // 3. Call Gemini AI
    const GEMINI_API_KEY = Deno.env.get('GEMINI_API_KEY')
    const response = await fetch(`https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=${GEMINI_API_KEY}`, {
      method: 'POST',
      body: JSON.stringify({
        contents: [{
          parts: [{
            text: `You are an energy expert. Based on these power logs: ${JSON.stringify(logs)}.
            Provide: 1. A forecast date when credit runs out. 2. A description of peak usage.
            3. Three specific savings tips. Return ONLY valid JSON matching this structure:
            {"forecastDate": "string", "peakUsageDescription": "string", "suggestions": ["string"]}`
          }]
        }]
      })
    })

    const result = await response.json()
    let aiText = result.candidates[0].content.parts[0].text

    // Clean up potential Markdown formatting from AI
    aiText = aiText.replace(/```json|```/g, "").trim()

    return new Response(aiText, {
      headers: { "Content-Type": "application/json" }
    })
  } catch (error) {
    return new Response(JSON.stringify({ error: error.message }), {
      status: 500,
      headers: { "Content-Type": "application/json" }
    })
  }
})
