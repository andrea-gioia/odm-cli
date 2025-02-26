# Open Data Mesh Command Line

## Abaut
This repository is the home of the Open Data Mesh CLI.

## Installation

Download the cli

```bash
echo TODO
```

or compile it from the code

```bash
git clone git@github.com:opendatamesh-initiative/odm-platform.git
cd odm-cli
mvn clean package spring-boot:repackage
cd odm-cli
```

test the cli

```bash
./odmcli --version
```

## Configuration
By default, the `odmcli` stores its configuration files in a directory called `.odmcli` within your `$HOME` directory.

`odmcli` manages most of the files in the configuration directory and you shouldn't modify them. However, you can modify the `config.json` file to control certain aspects of how the `odmcli` command behaves.

You can modify the `odmcli` command behavior using environment variables or command-line options. You can also use options within `config.json` to modify some of the same behavior. If an environment variable and the `--config` flag are set, the flag precedes the environment variable. Command line options override environment variables and environment variables override properties you specify in a `config.json` file.

## Usage

`odmcli [COMMAND] [OPTIONS] [ARGS]`

Manage local env and all interactions with the remote ODM Platform's services

## Commands

Command|Description
-------|----------
[`odmcli local`](docs/cmd-local.md)|Manage local env
[`odmcli registry`](docs/cmd-registry.md)|Interact with remote `registry-service`
[`odmcli blueprint`](docs/cmd-blueprint.md)|Interact with remote `blueprint-service`
[`odmcli devops`](docs/cmd-devops.md)|Interact with remote `devops-service`
[`odmcli policy`](docs/cmd-policy.md)|Interact with remote `policy-service`

## Options
TODO

### Common options

Command|Default|Description
-------|----------|-------
`--version, -v`||Version o the command
`--config`|`$HOME/.odmcli`|Location of odmcli config files
`-l, --log-level`|`info`|Set the logging level (`debug`, `info`, `warn`, `error`, `fatal`)
