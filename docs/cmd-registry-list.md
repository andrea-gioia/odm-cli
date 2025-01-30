# odmcli registry list

## Usage

`odmcli registry list`

## Description

List `registry-service` entities (i.e. `dp` or `dps`)

## Options

Option|Default|Description
-------|----------|-------
`--type`|`dp`|Type of the entity to list (`dp`\|`dpv`)
`--dp-id`||Id of data product. Applicable only if `type` is equal to dpv

## Examples

### List data products
```bash
./odmcli registry get --type dp
```

### List data product versions
```bash
./odmcli registry list --type dpv 
```
