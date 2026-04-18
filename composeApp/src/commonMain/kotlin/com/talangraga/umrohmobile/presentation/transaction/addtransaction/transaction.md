# Add Transaction Planning

In this screen will using LazyColumn to support scroll vertical and will have some item sections.
Each section will have its label.

This screen will separate to 3 main sections:
1. AddTransactionScreen
2. AddTransactionContent
3. Preview AddTransactionContent

### AddTransactionScreen
AddTransactionScreen will using to wrap AddTransactionContent, handle all the data source from viewmodel and external source.

### AddTransactionContent
Make this composable as dump as possible because this class to shows UI and not handle data source and only shows data from AddTransactionScreen.



## Bukti Transfer
    - Title Label: Bukti Transfer
    - Image loader shape: tap to capture or get image from gallery
## Anggota/Pengguna
    - Title Label: Anggota/Pengguna
    - Text label: Pilih Anggota, tap to show dropdown
## Tabungan ke:
    - Title Label: Tabungan ke:
    - Text label: Pilih Bulan, tap to show dropdown
## Metode Pembayaran
    - Title Label: Metode Pembayaran
    - Text label: Pilih metode pembayaran, tap to show dropdown
## Tanggal & Waktu
    - Title Label: Tanggal & Waktu
    - Text label: dd/mm/yyyy HH:mm, tap to show date picker
## Jumlah Tabungan
    - Title Label: Jumlah Tabungan
    - Text field: Rp xxx.xxx.xxx, tap to show keyboard
## Tambah Anggota
    - Title Label: Tambah Anggota
    - Text label: Tambah anggota, tap to show modal
## Daftar Anggota
    - Title Label: Daftar Anggota
    - List of joined member
    - Add jumlah tabungan on each member
## Tambah Transaksi
    - Button label: Tambah Transaksi
    - Button action: tap to add transaction by triggering add transaction API