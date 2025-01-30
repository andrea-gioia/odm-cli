# odmcli devops get

## Usage

`odmcli devops get`

## Description

Get an entity from the `devops-service`

## Options

Option|Default|Description
-------|----------|-------
`--type`||Type of the entity to get (`activity`\|`task`)
`--id`||Id of of the entity to get

## Examples

### Get task 123
```bash
./odmcli devops get --type task --id 123
```

