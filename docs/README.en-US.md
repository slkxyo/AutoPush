# AutoPush - Git Auto Sync Tool

English | [中文](README.zh-CN.md)

A command-line tool that automatically syncs local folders to GitHub, simplifying the Git commit and push workflow.

## Features

- 🚀 Automatically detects Git repository status
- 📦 Automatically initializes Git repository (if not exists)
- 🔄 Automatically adds, commits, and pushes changes
- 🆕 Automatically creates GitHub repository (if remote doesn't exist)
- 🔒 Supports creating public or private repositories
- 🎨 Colorful terminal output with clear execution status
- ⏰ Auto-generates commit messages with timestamps

## Prerequisites

Before using this tool, ensure the following tools are installed and configured:

### 1. Git
- Download: https://git-scm.com/downloads
- Verify installation: Run `git --version` in terminal

### 2. GitHub CLI (gh)
- Download: https://cli.github.com/
- Verify installation: Run `gh --version` in terminal
- **Important**: Must login to GitHub CLI first
  ```bash
  gh auth login
  ```
  Follow the prompts to complete GitHub account authorization

### 3. Java 17 or Higher
- Download: https://www.oracle.com/java/technologies/downloads/
- Verify installation: Run `java --version` in terminal

### 4. Maven (for building the project)
- Download: https://maven.apache.org/download.cgi
- Verify installation: Run `mvn --version` in terminal

## Installation & Build

1. Clone or download this project to your local machine

2. Execute the build command in the project root directory:
   ```bash
   mvn clean package
   ```

3. After building, an executable JAR file will be generated in the `target` directory

## Usage

### Basic Usage

Run in the folder you want to sync:

```bash
java -jar AutoPush-1.0-SNAPSHOT.jar
```

### Use Cases

#### Scenario 1: First Time Use (No Local Git Repository)
1. Run the tool
2. The tool will automatically initialize a Git repository
3. Prompt to enter remote repository name (e.g., `my-project`)
4. Prompt to choose repository visibility (enter `Y` for public, `N` for private)
5. Automatically creates GitHub repository and pushes code

#### Scenario 2: Existing Local Git Repository, No Remote
1. Run the tool
2. Prompt to enter remote repository name
3. Prompt to choose repository visibility (enter `Y` for public, `N` for private)
4. Automatically creates GitHub repository and pushes code

#### Scenario 3: Existing Local Git Repository with Remote
1. Run the tool
2. Automatically pulls latest changes from remote repository (`git pull`)
3. Automatically adds all changes, commits, and pushes to remote repository

## Sync Logic

The tool's execution flow:

```
Start
  ↓
Check if Git and GitHub CLI are ready
  ↓
  ├─ Not ready → Prompt to install and exit
  ↓
  └─ Ready → Continue
       ↓
Check if current directory is a Git repository
  ↓
  ├─ Not a Git repository
  │    ↓
  │    1. Execute git init (initialize repository)
  │    2. Execute git add -A (add all files)
  │    3. Execute git commit (commit with timestamp)
  │    4. Execute git branch -m main (rename branch to main)
  │    5. Prompt user to enter remote repository name
  │    6. Prompt user to choose repository visibility (Y=public, N=private)
  │    7. Execute gh repo create (create GitHub repository and push)
  │    ↓
  │    Done
  │
  └─ Is a Git repository
       ↓
  Check if remote repository is configured
       ↓
       ├─ No remote configured
       │    ↓
       │    1. Execute git add -A
       │    2. Execute git commit (commit with timestamp)
       │    3. Execute git branch -m main
       │    4. Prompt user to enter remote repository name
       │    5. Prompt user to choose repository visibility (Y=public, N=private)
       │    6. Execute gh repo create (create GitHub repository and push)
       │    ↓
       │    Done
       │
       └─ Remote configured
            ↓
            1. Execute git pull (pull remote changes)
            2. Execute git add -A (add all changes)
            3. Execute git commit (commit with timestamp)
            4. Execute git push (push to remote repository)
            ↓
            Done
```

### Detailed Steps

1. **Environment Check**
   - Check if Git is installed (`git version`)
   - Check if GitHub CLI is logged in (`gh auth status`)

2. **Repository Status Detection**
   - Execute `git status` to determine if it's a Git repository

3. **Auto Commit**
   - Use `git add -A` to add all changes
   - Use formatted timestamp as commit message: `YYYY-MM-D hh:mm:ss Auto Commit`

4. **Remote Repository Handling**
   - If no remote repository:
     - Prompt user to enter repository name
     - Prompt user to choose visibility (enter `Y` for public, anything else for private)
     - Use `gh repo create` to create GitHub repository (`--public` or `--private`)
   - If remote exists:
     - First use `git pull` to fetch latest remote changes
     - Then use `git push` to push local changes

5. **Status Feedback**
   - Green: Success messages
   - Yellow: Executed commands
   - Red: Error messages
   - White: Command output

## Technical Implementation

### Core Dependencies

- **JLine 3.30.0**: Provides colorful terminal output and interactive command-line interface
- **Lombok 1.18.30**: Simplifies Java code writing
- **SLF4J + Logback**: Logging

### Main Classes

- `Main.java`: Main program entry, contains sync logic
  - `pushToRemote()`: Handles remote repository creation, supports user choice of public/private
  - `isReady()`: Checks Git and GitHub CLI environment
  - `readLine()`: Interactive user input reading
  - `exec()`: Executes Git commands
  - `execWithOutput()`: Executes commands and returns output
- `Writer.java`: Terminal output utility class, supports colored text display

## Important Notes

1. ⚠️ The tool automatically executes `git add -A`, which adds all files (including untracked files)
2. ⚠️ It's recommended to configure a `.gitignore` file before use to exclude unnecessary files
3. ⚠️ When creating a GitHub repository, you can choose public or private:
   - Enter `Y` or `y` to create a public repository
   - Enter `N`, `n`, or anything else to create a private repository
4. ⚠️ Commit messages are auto-generated in the format: `YYYY-MM-D hh:mm:ss Auto Commit`
5. ⚠️ Ensure GitHub CLI is properly logged in, otherwise remote repository creation will fail

## FAQ

### Q: Getting "Please ensure that Git and Github CLI is installed"
**A:** Please check:
- Git is installed and added to system PATH
- GitHub CLI is installed
- GitHub CLI is logged in (run `gh auth login`)

### Q: Push fails with "Error!"
**A:** Possible reasons:
- Network connection issues
- GitHub authentication expired, need to re-login
- Remote repository permission issues
- Repository name already exists

### Q: How to modify commit message format?
**A:** Modify the `DateTimeFormatter.ofPattern()` parameter in `Main.java`

## License

This project is for learning and personal use only.

## Contributing

Issues and Pull Requests are welcome!
