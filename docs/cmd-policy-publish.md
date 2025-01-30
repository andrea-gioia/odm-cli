# odmcli policy publish

## Usage

`odmcli policy publish`

## Description

Publish an entity in the `policy-service`
   
## Options

Option|Default|Description
-------|----------|-------
`--entity-file`|| File that contains the entity description
`--type`||Type of the entity to publish (`policy`\|`engine`|`eval`)

## Examples

### Publish a policy
```bash
./odmcli policy publish --type policy --entity-file policy.json
```



