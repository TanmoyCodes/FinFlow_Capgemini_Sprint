try {
    $r = Invoke-WebRequest -Uri 'http://localhost:8081/application/v3/api-docs' -UseBasicParsing
    Write-Output $r.Content
} catch {
    $s = [System.IO.StreamReader]::new($_.Exception.Response.GetResponseStream())
    Write-Output $s.ReadToEnd()
}
