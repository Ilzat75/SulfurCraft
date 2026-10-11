foreach ($n in @('sunflower_stage0','sunflower_stage1','sunflower_stage2_lower','sunflower_stage2_upper','sunflower_stage3_lower','sunflower_stage3_upper')) {
  $p = "C:\Users\User\Desktop\SulfurCraft\work\stage\assets\sulfurcraft\models\block\$n.json"
  $t = Get-Content $p -Raw
  $petals = [regex]::Matches($t, '#petals[a-z_]*') | ForEach-Object { $_.Value } | Select-Object -Unique
  $seed   = [regex]::Matches($t, '#seed[a-z_]*')   | ForEach-Object { $_.Value } | Select-Object -Unique
  $leaf   = [regex]::Matches($t, '#leaf[a-z_]*')   | ForEach-Object { $_.Value } | Select-Object -Unique
  Write-Host "$n : petals=$($petals -join '/') seed=$($seed -join '/') leaf=$($leaf -join '/')"
}
