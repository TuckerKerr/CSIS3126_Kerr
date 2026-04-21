1. Set Up Android Project
Open Android Studio |
Import the provided project (recommended due to deep file structure) |
OR create a new project using: Minimum SDK: API 24 (Nougat) |
Make sure all folders/files are placed exactly as provided (paths matter)

2. Set Up MAMP (Backend)
Install and open MAMP |
Navigate to the htdocs folder inside MAMP |
Create a folder named: RootedGardening |
Place all the provided PHP files inside this folder

3. Set Up the Database
Start MAMP |
Load MySQL Workbench and create a new database (Name provided in the SQL Dump) |
Use the provided SQL dump files: |
Copy and paste each SQL query |
Run them to create the tables and structure

4. Configure API Keys
Locate where API keys are used in the project (provided separately) |
Insert your API keys file in the RootedGardening PHP file

5. Run the Backend
Start servers in MAMP (Apache + MySQL) |
Confirm your backend is running by visiting: http://localhost:8888/RootedGardening/

6. Run the Android App
Open the project in Android Studio |
Let Gradle sync |
Run the app on an emulator or device

Important Notes!!!
You must have internet access (app uses multiple external APIs) |
Do not change file paths, as the app depends on them |
Ensure PHP files are correctly placed in htdocs/RootedGardening 
