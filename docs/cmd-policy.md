# odmcli policy

## Usage

`odmcli policy`

## Description

Interact with remote `policy-service`

## Options

Option|Default|Description
-------|----------|-------
`--service-url`||URL of the `policy-service`. It must include the port. It overrides the value inside the properties file if it is present

## Subcommands

Command|Description
-------|----------
[`odmcli policy list`](cmd-policy-list.md)|List entities from `policy-service`
[`odmcli policy get`](cmd-policy-get.md)|Get an entity from `policy-service`
[`odmcli policy publish`](cmd-policy-publish.md)|Publish an entity from `policy-service`
[`odmcli policy update`](cmd-policy-update.md)|Update an entity on `policy-service`
[`odmcli policy validate`](cmd-policy-validate.md)|Validate a document with `policy-service`

