Add-Type -AssemblyName System.IO.Compression
Add-Type -AssemblyName System.IO.Compression.FileSystem
$jar = 'C:\Users\User\Desktop\SulfurCraft\sulfurcraft-1.2.jar'
$c = [System.IO.Compression.ZipFile]::OpenRead($jar)
$names = @($c.Entries | ForEach-Object { $_.FullName } | Sort-Object)
$c.Dispose()
Write-Host "--- entries ($($names.Count)) ---"
$names | ForEach-Object { Write-Host $_ }
