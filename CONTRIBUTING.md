# Contributing to BridgeSplash

Thank you for your interest in contributing! Please follow these guidelines to help us maintain a high-quality codebase.

## Branching

- **Main branch:** `main` is the stable branch. Do not commit directly to `main`.
- **Feature branches:** Create a new branch for each feature or fix. Use the format:  
  `feature/your-feature-name` or `fix/your-bug-description`.
- **Miscellaneous:** For small changes or experiments, use `misc/your-description`.
- **Pull Requests:** Always open a pull request (PR) to merge your branch into `main`. Ensure your branch is up to date with `main` before submitting a PR.

## Code Formatting

- **Java/Kotlin:**
    - Use the default IntelliJ IDEA code style for Java and Kotlin.
    - Indent with 4 spaces.
    - Use braces `{}` for all control structures.
    - Add Javadoc for all public classes and methods.
- **General:**
    - Remove unused imports and variables.
    - Keep lines under 120 characters where possible.

## Commit Messages

- Use clear, descriptive commit messages.
- Start with a verb in the present tense (e.g., `Add`, `Fix`, `Update`).
- Reference issues or PRs when relevant.

### Commitlint and Conventional Commits

We use **commitlint** to enforce [Conventional Commit](https://www.conventionalcommits.org/) formatting for commit messages. This helps automate changelogs and improves readability.

**Commit message format:**
```
type(scope?): subject
```
- `type`: The kind of change (see list below).
- `scope`: *(optional)* The area of the codebase affected (e.g., `api`, `ui`, `server`). Multiple scopes can be separated by `/`, `\`, or `,`.
- `subject`: Short description of the change (imperative, lower case).

**Examples:**
```
chore: run tests on travis ci
fix(server): send cors headers
feat(blog): add comment section
```

**Common types (from [commitlint-config-conventional](https://github.com/conventional-changelog/commitlint/tree/master/@commitlint/config-conventional)):**
- `build`: Changes that affect the build system or external dependencies
- `chore`: Other changes that don't modify src or test files
- `ci`: Changes to CI configuration files and scripts
- `docs`: Documentation only changes
- `feat`: A new feature
- `fix`: A bug fix
- `perf`: A code change that improves performance
- `refactor`: A code change that neither fixes a bug nor adds a feature
- `revert`: Reverts a previous commit
- `style`: Changes that do not affect the meaning of the code (white-space, formatting, etc)
- `test`: Adding missing tests or correcting existing tests

**Tips:**
- Keep the subject line under 72 characters.
- Use the body to explain *why* the change was made, if necessary.
- Example with scope: `fix(api): handle null pointer exception`
- Example with multiple scopes: `feat(api/ui): add user profile page`

Your commits will be checked automatically. If your message does not follow the format, the commit will be rejected.

## Pull Request Checklist

- [ ] Code compiles and passes all tests.
- [ ] Follows code style and formatting guidelines.
- [ ] Includes relevant documentation and Javadocs.
- [ ] Describes the purpose and changes in the PR description.

## Communication

- Use GitHub Issues for bugs and feature requests.
- Be respectful and constructive in all discussions.

Thank you for helping improve BridgeSplash!