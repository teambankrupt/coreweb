package com.example.coreweb.scheduling.templates

fun reminderTemplate(subject: String, message: String) = """
    
    <!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reminder: ${subject}</title>
    <style>
        body {
            font-family: sans-serif;
            margin: 0;
            padding: 20px;
            text-align: center;
        }
        .container {
            width: 80%;
            max-width: 600px;
            margin: 0 auto;
        }
        h1 {
            font-size: 24px;
            margin-bottom: 15px;
        }
        .reminder-msg {
            font-size: 20px;
            font-weight: bold;
            background-color: #f0f0f0;
            padding: 10px 20px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        p {
            font-size: 16px;
            line-height: 1.5;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>$subject</h1>
        <div class="reminder-msg">$message</div>
        <p>Thanks</p>
    </div>
</body>
</html>
    
"""