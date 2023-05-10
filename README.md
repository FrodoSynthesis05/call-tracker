
Java & Maven Call Center Tracking Software

As an interpreter paid by the minute for a company without an internal tool to track daily earnings, I developed my own solution. The Java program uses MaterialLookAndFeel for a dark theme. Additionally, I added a window that can be accessed through a hotkey, which natively queries the database to show earnings for a particular day along with all the call details stored in the database. The software also includes an override window that allows the user to modify all the information associated with a particular call if needed.

When the Start behavior is triggered, the program starts an internal timer and takes a screenshot of the main monitor. The image is analyzed using Tesseract, an Optical Character Recognition engine developed by Google, which returns the call ID. This ID is then uploaded to a locally hosted SQL database. I have also added hotkey support through JIntellitype that listens for system-wide commands such as "Alt + S" to open the query window and "Ctrl + O" to open the override window. Keys ";" and " ' " control the start and stop behavior no matter which window the user is focused on.

When the Stop behavior is triggered, the timer is stopped and the software calculates the monetary value associated with the call in US Dollars based on the duration of the call. This value is then converted to Colombian Pesos using real-time exchange rate information provided by the openexchangerates.com API. A custom key is required to use this API.

With the help of phpMyAdmin, the user can consult the information associated with a particular call by looking up its ID. Additionally, simple SQL statements can be used to calculate estimated daily, monthly, and yearly income. This tool can be accessed by right clicking the system tray icon for the application, place where you will also find a Clear Screenshot folder and a Quit function.

The SQL script required to create a database that works in conjunction with this program can be found under the data folder on the script.sql file.