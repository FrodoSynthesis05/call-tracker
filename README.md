

# CallTrack Pro
The Call Center Tracking Software is a Java program developed to track daily earnings for interpreters who are paid by the minute. This software provides a solution for companies without an internal tracking tool. The program utilizes Java and Maven for development and includes various features to streamline the tracking process.

# Features
- Dark-themed user interface using MaterialLookAndFeel.
- Window accessed through a hotkey to query the database for daily earnings and call details.
- Override window for modifying call information.
- Start behavior triggers an internal timer and captures a screenshot of the main monitor.
- Optical Character Recognition (OCR) using Tesseract to extract call ID from the screenshot.
- Call ID is uploaded to a locally hosted SQL database.
- Hotkey support with JIntellitype for system-wide commands.
- Stop behavior calculates the monetary value of the call based on its duration.
- Real-time exchange rate conversion from US Dollars to Colombian Pesos using a custom API.
- Database management with phpMyAdmin to query call information.
- SQL statements for calculating estimated daily, monthly, and yearly income.
- System tray icon with options to access the tool, clear screenshots, and quit.
- Double-click system tray icon to show/hide the Start/Stop button UI.
# Prerequisites
Before using the Call Center Tracking Software, make sure you have the following:

- Java JDK installed.
- Maven installed.
- WAMP server installed and configured.
- Tesseract OCR engine installed.
- API key for openexchangerates.com (required for real-time exchange rate conversion).

# Usage
1. Copy the path for wampmanager.exe and paste it into the config.txt file.
2. Set the destination path for storing images in the config.txt file.
3. Set the path to the iconpack folder in the config.txt file.
4. Set the openexchangerates.com API key in the config.txt file.
5. Set the path to the tessdata folder in the OCR engine installation directory in the config.txt file.
6. Run the SQL script (script.sql) to create the required database.
7. Launch the Call Center Tracking Software.
8. Use the hotkeys to open the query window or the override window.
9. Start the behavior by pressing the ";" key and stop it by pressing the "'" key.
10. Access call information and perform calculations using phpMyAdmin.
11. Customize the software through the system tray icon.
# Troubleshooting

If you encounter any issues while using the Call Center Tracking Software, consider the following:

- Verify that Java JDK and Maven are installed correctly.
- Check the WAMP server configuration and ensure it is running.
- Make sure the Tesseract OCR engine is installed properly.
- Ensure the paths in the config.txt file are correct.
- Verify the availability and validity of the openexchangerates.com API key.
# License
The Call Center Tracking Software is released under the MIT License. See the [LICENSE](https://github.com/FrodoSynthesis05/call-tracker/blob/master/LICENSE) file for more details.

# Credits
The Call Center Tracking Software was developed by Julian Baquero. It incorporates various libraries and resources, including:

- MaterialLookAndFeel for the dark-themed user interface.
- JIntellitype for system-wide hotkey support.
- Tesseract OCR engine for optical character recognition.
- [currency-api](https://github.com/fawazahmed0/currency-api) for real-time exchange rate information.
# Contributing
Contributions to the Call Center Tracking Software are welcome! If you find any bugs, have feature requests, or would like to contribute enhancements, please submit an issue or a pull request.

# Feedback
I appreciate your feedback! If you have any questions, suggestions, or comments, please feel free to [contact us/email us/etc.].

# Acknowledgments
I would like to express my gratitude to the developers of the WAMP server, Tesseract OCR engine, and all the libraries used in this project for their contributions.

I hope that the Call Center Tracking Software proves to be a helpful tool for tracking earnings efficiently!
