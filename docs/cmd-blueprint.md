# odmcli blueprint

## Usage

`odmcli blueprint`

## Description

Interact with remote `blueprint-service`

## Options

Option|Default|Description
-------|----------|-------
`--service-url`||URL of the `blueprint-service`. It must include the port. It overrides the value inside the properties file if it is present

## Subcommands

Command|Description
-------|----------
[`odmcli blueprint list`](cmd-blueprint-list.md)|List entities from `blueprint-service`
[`odmcli blueprint get`](cmd-blueprint-get.md)|Get an entity from `blueprint-service`
[`odmcli blueprint publish`](cmd-blueprint-publish.md)|Publish an entity from `blueprint-service`

