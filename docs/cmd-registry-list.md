# odmcli repository list

## Usage

`odmcli repository list`

## Description

List `repository-service` entities (i.e. dp or dps)

**⚒️Implementation details:**

1. TODO

## Options

Option|Default|Description
-------|----------|-------
`--type`||Type of the entity to list (dp\|dpv)
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
