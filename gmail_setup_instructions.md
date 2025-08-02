# Setting up Gmail for RainCheck Notifications

## Creating an App Password for Gmail

Since Gmail no longer supports "Less secure apps", you'll need to use an App Password:

1. Go to your Google Account settings: https://myaccount.google.com/
2. Select "Security" from the left menu
3. Under "Signing in to Google," select "2-Step Verification" (you must enable this first)
4. At the bottom of the page, select "App passwords"
5. Generate a new app password for "Mail" and "Other (Custom name)" - name it "RainCheck"
6. Copy the 16-character password that appears
7. Paste this password into your `config/secrets.json` file for the `email_password` field

## Configuration in secrets.json

```json
{
  "email_username": "your.email@gmail.com",
  "email_password": "your-16-character-app-password"
}
```

## Troubleshooting

If you encounter issues:

1. Make sure 2-Step Verification is enabled on your Google account
2. Ensure you're using the app password, not your regular Google password
3. Check the console for SMTP error messages
4. Try enabling "Less secure app access" temporarily for testing: https://myaccount.google.com/lesssecureapps
