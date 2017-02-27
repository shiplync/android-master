# android
Traansmission Android app

# android
Traansmission Android app

1. Install Android Studio

2. In Android Studio, open this project and install dependencies as necessary

3. Install Genymotion

4. Start Genymotion using recommended device: Samsung Galaxy S4 - 4.4.4 - API 19

5. Install Google Play Service in the Genymotion emulator:
https://github.com/codepath/android_guides/wiki/Genymotion-2.0-Emulators-with-Google-Play-support

6. Configure the Android studio to use Genymotion and run the application:
https://docs.genymotion.com/pdf/PDF_Plugin_for_Android_Studio/Plugin-for-Android-Studio-1.0.7-Guide.pdf



Send shipment notification:

curl -X POST --header "Authorization:key=[API_key]" --header Content-Type:"application/json" https://gcm-http.googleapis.com/gcm/send -d "{\"registration_ids\":[\"[token]\"], \"data\":{\"message\":\"[[message]]\", \"email\":\"[email]\", \"shipment_id\":\"[shipment_id]\", \"title\":\"[title]]\"}}"

Send location request:

curl -X POST --header "Authorization:key=[API_key]" --header Content-Type:"application/json" https://gcm-http.googleapis.com/gcm/send -d "{\"registration_ids\":[\"[token]\"], \"data\":{\"location_broadcast\":\"this field doesn't matter\"}}"



