Add-Type -AssemblyName System.IO.Compression
Add-Type -AssemblyName System.IO.Compression.FileSystem
$jar = 'C:\Users\User\Desktop\SulfurCraft\sulfurcraft-1.2.jar'
$c = [System.IO.Compression.ZipFile]::OpenRead($jar)
$all = @($c.Entries | ForEach-Object { $_.FullName })
$bs = @($all | Where-Object { $_ -like '*\*' }).Count
Write-Host "total=$($all.Count) backslashes=$bs"
$all | Where-Object { $_ -match 'gunpowder|patty|tacz|pack.mcmeta|SulfurCraft.class' } | ForEach-Object { Write-Host $_ }
$c.Dispose()
