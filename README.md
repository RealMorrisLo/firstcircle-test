# First Circle Banking Service

A simple in-memory banking service written in Kotlin. Supports account creation, deposits, withdrawals, and transfers between accounts.

## Requirements

- JDK 21+

> No need to install Gradle — the project includes `gradlew`, a self-contained wrapper that downloads the correct Gradle version automatically.

## Install JDK 21

**macOS (Homebrew)**
```bash
brew install openjdk@21
```

**Any OS (SDKMAN)**
```bash
# Install SDKMAN first (if you don't have it)
curl -s "https://get.sdkman.io" | bash

# Then install JDK 21
sdk install java 21-tem
```

Verify the installation:
```bash
java -version
```

## Run the app

```bash
./gradlew run --console=plain
```

This starts an interactive CLI. Type `help` to see available commands.

## Commands

```
create-user <firstName> <lastName> [initialBalance]  - Open a new account
get-user <userId>                                     - Look up a user
balance <userId>                                      - Check account balance
deposit <userId> <amount>                             - Deposit money
withdraw <userId> <amount>                            - Withdraw money
transfer <fromUserId> <toUserId> <amount>             - Transfer between accounts
help                                                  - Show available commands
exit                                                  - Quit
```

## Example session

```
> create-user John Doe 500.00
Created: id=a1b2c3d4-...  name=John Doe  balance=500.00

> create-user Jane Smith 200.00
Created: id=e5f6g7h8-...  name=Jane Smith  balance=200.00

> balance a1b2c3d4-...
Balance [a1b2c3d4-...]: 500.00

> deposit a1b2c3d4-... 100.00
Deposited 100.00 -> new balance: 600.00

> withdraw a1b2c3d4-... 50.00
Withdrew 50.00 -> new balance: 550.00

> transfer a1b2c3d4-... e5f6g7h8-... 150.00
Transferred 150.00 from a1b2c3d4-... to e5f6g7h8-...

> exit
Goodbye!
```

## Run tests

```bash
./gradlew test
```
