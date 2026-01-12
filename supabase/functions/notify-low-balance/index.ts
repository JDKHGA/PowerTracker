import { serve } from "https://deno.land/std@0.168.0/http/server.ts"
import { createClient } from 'https://esm.sh/@supabase/supabase-js@2'

const supabaseUrl = Deno.env.get('SUPABASE_URL')!
const supabaseServiceKey = Deno.env.get('SUPABASE_SERVICE_ROLE_KEY')!

const supabase = createClient(supabaseUrl, supabaseServiceKey)

serve(async (req) => {
  try {
    const payload = await req.json()
    const { record, old_record } = payload

    // Only proceed if balance decreased
    if (old_record && record.balance_kwh >= old_record.balance_kwh) {
      return new Response(JSON.stringify({ message: 'Balance did not decrease' }), { status: 200 })
    }

    // 1. Fetch user settings
    const { data: settings, error: settingsError } = await supabase
      .from('user_settings')
      .select('*')
      .eq('user_id', record.user_id)
      .single()

    if (settingsError || !settings) {
      return new Response(JSON.stringify({ error: 'Settings not found' }), { status: 404 })
    }

    // 2. Check if notifications are enabled and balance is below threshold
    if (settings.notifications_enabled && record.balance_kwh <= settings.alert_threshold) {

      // 3. Fetch device tokens
      const { data: tokens, error: tokensError } = await supabase
        .from('device_tokens')
        .select('fcm_token')
        .eq('user_id', record.user_id)

      if (tokensError || !tokens || tokens.length === 0) {
        return new Response(JSON.stringify({ message: 'No device tokens found' }), { status: 200 })
      }

      const fcmTokens = tokens.map(t => t.fcm_token)

      // 4. Send Notification
      // Since we are not using Firebase directly in the app,
      // you can use Supabase's built-in push notification support if you configure a provider.
      // Alternatively, you can use a service like OneSignal or Courier here.

      console.log(`ALARM: Low balance for user ${record.user_id}. Meter: ${record.name}. Balance: ${record.balance_kwh} kWh`)

      // SIMULATION: In a real app, you would call FCM or another push service here.
      // For this project, we'll simulate success.
      console.log(`Sending push notifications to ${fcmTokens.length} devices...`)
      
      /*
      // Real implementation example with FCM:
      const response = await fetch('https://fcm.googleapis.com/fcm/send', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `key=${Deno.env.get('FCM_SERVER_KEY')}`
        },
        body: JSON.stringify({
          registration_ids: fcmTokens,
          notification: {
            title: "Low Balance Alert",
            body: `Your meter ${record.name} has only ${record.balance_kwh} kWh remaining.`
          }
        })
      })
      */
    }

    return new Response(JSON.stringify({ message: 'Processed successfully' }), { status: 200 })
  } catch (error) {
    return new Response(JSON.stringify({ error: error.message }), { status: 500 })
  }
})
