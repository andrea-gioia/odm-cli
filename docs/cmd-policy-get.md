# odmcli policy get

## Usage

`odmcli policy get`

## Description

Get an entity from the `policy-service`

## Options

Option|Default|Description
-------|----------|-------
`--type`||Type of the entity to get (`policy`\|`engine``\|`eval`)
`--id`||Id of of the entity to get

## Examples

### Get policy 123
```bash
./odmcli policy get --type policy --id 123
```

