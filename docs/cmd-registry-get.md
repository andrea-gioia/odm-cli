# odmcli registry get

## Usage

`odmcli registry get`

## Description

Get an entity from the registry

**⚒️Implementation details:**

1. TODO

## Options

Option|Default|Description
-------|----------|-------
`--type`||Type of the entity to get (dp\|dpv)
`--dp-id`||Id of data product. Applicable only if `type` is equal to dpv
`--dp-version`||Version of the data product. Applicable only if `type` is equal to dpv

## Examples

### Get data product 123
```bash
./odmcli registry get --type dp --id 123
```

### Get version 2.0.0 of data product 123
```bash
./odmcli registry get --type dpv --id 123 --dp-version 2.0.0
```
