# odmcli devops

## Usage

`odmcli devops`

## Description

Interact with remote `devops-service`

## Options

Option|Default|Description
-------|----------|-------
`--service-url`||URL of the devops-service. It must include the port. It overrides the value inside the properties file if it is present

## Subcommands

Command|Description
-------|----------
[`odmcli devops list`](cmd-devops-list.md)|List entities from `devops-service`
[`odmcli devops get`](cmd-devops-get.md)|Get an entity from `devops-service`
[`odmcli devops publish`](cmd-devops-publish.md)|Publish an entity from `devops-service`
[`odmcli devops start`](cmd-devops-start.md)|Start an runnable entity managed by `devops-service`
[`odmcli devops stop`](cmd-devops-stop.md)|Stop an runnable entity managed by `devops-service`

