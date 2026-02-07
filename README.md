# AutoPush - Git 自动同步工具

一个自动将本地文件夹同步到 GitHub 的命令行工具，简化 Git 提交和推送流程。

## 功能特点

- 🚀 自动检测 Git 仓库状态
- 📦 自动初始化 Git 仓库（如果不存在）
- 🔄 自动添加、提交和推送更改
- 🆕 自动创建 GitHub 仓库（如果远程仓库不存在）
- 🔒 支持选择创建公开或私有仓库
- 🎨 彩色终端输出，清晰显示执行状态
- ⏰ 自动生成带时间戳的提交信息

## 前置要求

在使用本工具之前，请确保已安装并配置以下工具：

### 1. Git
- 下载地址：https://git-scm.com/downloads
- 验证安装：在终端运行 `git --version`

### 2. GitHub CLI (gh)
- 下载地址：https://cli.github.com/
- 验证安装：在终端运行 `gh --version`
- **重要**：必须先登录 GitHub CLI
  ```bash
  gh auth login
  ```
  按照提示完成 GitHub 账号授权

### 3. Java 17 或更高版本
- 下载地址：https://www.oracle.com/java/technologies/downloads/
- 验证安装：在终端运行 `java --version`

### 4. Maven（用于构建项目）
- 下载地址：https://maven.apache.org/download.cgi
- 验证安装：在终端运行 `mvn --version`

## 安装与构建

1. 克隆或下载本项目到本地

2. 在项目根目录下执行构建命令：
   ```bash
   mvn clean package
   ```

3. 构建完成后，在 `target` 目录下会生成可执行的 JAR 文件

## 使用方法

### 基本用法

在需要同步的文件夹中运行：

```bash
java -jar AutoPush-1.0-SNAPSHOT.jar
```

### 使用场景

#### 场景 1：首次使用（本地无 Git 仓库）
1. 运行工具
2. 工具会自动初始化 Git 仓库
3. 提示输入远程仓库名称（例如：`my-project`）
4. 提示选择仓库可见性（输入 `Y` 创建公开仓库，输入 `N` 创建私有仓库）
5. 自动创建 GitHub 仓库并推送代码

#### 场景 2：已有本地 Git 仓库，但未关联远程仓库
1. 运行工具
2. 提示输入远程仓库名称
3. 提示选择仓库可见性（输入 `Y` 创建公开仓库，输入 `N` 创建私有仓库）
4. 自动创建 GitHub 仓库并推送代码

#### 场景 3：已有本地 Git 仓库且已关联远程仓库
1. 运行工具
2. 自动添加所有更改、提交并推送到远程仓库

## 同步逻辑

工具的执行流程如下：

```
开始
  ↓
检查 Git 和 GitHub CLI 是否就绪
  ↓
  ├─ 未就绪 → 提示安装并退出
  ↓
  └─ 已就绪 → 继续
       ↓
检查当前目录是否为 Git 仓库
  ↓
  ├─ 不是 Git 仓库
  │    ↓
  │    1. 执行 git init（初始化仓库）
  │    2. 执行 git add -A（添加所有文件）
  │    3. 执行 git commit（提交，附带时间戳）
  │    4. 执行 git branch -m main（重命名分支为 main）
  │    5. 提示用户输入远程仓库名称
  │    6. 提示用户选择仓库可见性（Y=公开，N=私有）
  │    7. 执行 gh repo create（创建 GitHub 仓库并推送）
  │    ↓
  │    完成
  │
  └─ 是 Git 仓库
       ↓
  检查是否已配置远程仓库
       ↓
       ├─ 未配置远程仓库
       │    ↓
       │    1. 执行 git add -A
       │    2. 执行 git commit（提交，附带时间戳）
       │    3. 执行 git branch -m main
       │    4. 提示用户输入远程仓库名称
       │    5. 提示用户选择仓库可见性（Y=公开，N=私有）
       │    6. 执行 gh repo create（创建 GitHub 仓库并推送）
       │    ↓
       │    完成
       │
       └─ 已配置远程仓库
            ↓
            1. 执行 git add -A（添加所有更改）
            2. 执行 git commit（提交，附带时间戳）
            3. 执行 git push（推送到远程仓库）
            ↓
            完成
```

### 详细步骤说明

1. **环境检查**
   - 检查 Git 是否安装（`git version`）
   - 检查 GitHub CLI 是否已登录（`gh auth status`）

2. **仓库状态检测**
   - 执行 `git status` 判断是否为 Git 仓库

3. **自动提交**
   - 使用 `git add -A` 添加所有更改
   - 使用格式化的时间戳作为提交信息：`YYYY-MM-D hh:mm:ss Auto Commit`

4. **远程仓库处理**
   - 如果没有远程仓库：
     - 提示用户输入仓库名称
     - 提示用户选择可见性（输入 `Y` 创建公开仓库，其他输入创建私有仓库）
     - 使用 `gh repo create` 创建 GitHub 仓库（`--public` 或 `--private`）
   - 如果已有远程仓库，直接使用 `git push` 推送

5. **状态反馈**
   - 绿色：成功信息
   - 黄色：执行的命令
   - 红色：错误信息
   - 白色：命令输出

## 技术实现

### 核心依赖

- **JLine 3.30.0**：提供彩色终端输出和交互式命令行界面
- **Lombok 1.18.30**：简化 Java 代码编写
- **SLF4J + Logback**：日志记录

### 主要类说明

- `Main.java`：主程序入口，包含同步逻辑
  - `pushToRemote()`：处理远程仓库创建，支持用户选择公开/私有
  - `isReady()`：检查 Git 和 GitHub CLI 环境
  - `readLine()`：交互式读取用户输入
  - `exec()`：执行 Git 命令
  - `execWithOutput()`：执行命令并返回输出
- `Writer.java`：终端输出工具类，支持彩色文本显示

## 注意事项

1. ⚠️ 工具会自动执行 `git add -A`，会添加所有文件（包括未跟踪的文件）
2. ⚠️ 建议在使用前配置 `.gitignore` 文件，排除不需要提交的文件
3. ⚠️ 创建 GitHub 仓库时可以选择公开或私有：
   - 输入 `Y` 或 `y` 创建公开仓库
   - 输入 `N`、`n` 或其他任何内容创建私有仓库
4. ⚠️ 提交信息自动生成，格式为：`YYYY-MM-D hh:mm:ss Auto Commit`
5. ⚠️ 确保 GitHub CLI 已正确登录，否则无法创建远程仓库

## 常见问题

### Q: 提示 "Please ensure that Git and Github CLI is installed"
**A:** 请检查：
- Git 是否已安装并添加到系统 PATH
- GitHub CLI 是否已安装
- GitHub CLI 是否已登录（运行 `gh auth login`）

### Q: 推送失败显示 "Error!"
**A:** 可能的原因：
- 网络连接问题
- GitHub 认证过期，需要重新登录
- 远程仓库权限问题
- 仓库名称已存在

### Q: 如何修改提交信息格式？
**A:** 修改 `Main.java` 中的 `DateTimeFormatter.ofPattern()` 参数

## 许可证

本项目仅供学习和个人使用。

## 贡献

欢迎提交 Issue 和 Pull Request！
