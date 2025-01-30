# odmcli policy list

## Usage

`odmcli policy list`

## Description

List `policy-service` entities (i.e. `policy`, `engine` or `evaluation`)

## Options

Option|Default|Description
-------|----------|-------
`--type`|`dp`|Type of the entity to list (`policy`\|`engine`\|`eval`)

## Examples

### List policies
```bash
./odmcli policy list --type policy
```
