# AutoPush - Git Auto Sync Tool

<div align="center">

**A command-line tool that automatically syncs local folders to GitHub**

[English](docs/README.en-US.md) · [简体中文](docs/README.zh-CN.md)

</div>

---

## Quick Start

```bash
# Build the project
mvn clean package

# Run the tool
java -jar target/AutoPush-1.0-SNAPSHOT.jar
```

## Features

- 🚀 Automatically detects Git repository status
- 📦 Automatically initializes Git repository (if not exists)
- 🔄 Automatically adds, commits, and pushes changes
- 🆕 Automatically creates GitHub repository (if remote doesn't exist)
- 🔒 Supports creating public or private repositories
- 🎨 Colorful terminal output with clear execution status
- ⏰ Auto-generates commit messages with timestamps

## Prerequisites

- Git
- GitHub CLI (gh) - must be logged in
- Java 17 or higher
- Maven

## Documentation

For detailed documentation, please refer to:

- [English Documentation](docs/README.en-US.md)
- [中文文档](docs/README.zh-CN.md)

## License

This project is for learning and personal use only.
