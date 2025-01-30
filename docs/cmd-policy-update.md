# odmcli policy update

## Usage

`odmcli policy update`

## Description

Update an entity in the `policy-service`
   
## Options

Option|Default|Description
-------|----------|-------
`--entity-file`|| File that contains the entity description
`--type`||Type of the entity to update (`policy`\|`engine`\|`eval`)
`--id`||Id of the entity to update 

## Examples

### Update policy 123
```bash
./odmcli policy update --type policy --id 123 --entity-file policy.json
```



