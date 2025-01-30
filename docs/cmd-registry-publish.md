# odmcli registry publish

## Usage

`odmcli registry publish`

## Description

Publish an entity in the `registry-service`
   
## Options

Option|Default|Description
-------|----------|-------
`-f, --file`|| File that contains the definition of the entity to publish
`--type`||Type of the entity to publish (`dp`\|`dpv`)
`--id`||Id of data product. Applicable only if `type` is equal to `dpv`

## Examples

### Publish a local descriptor in the registry
```bash
./odmcli registry publish --type dpv --id dp123 -f dpd/data-product-descriptor.json
```



