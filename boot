100 DEV_USE 8,"win1_"
110 PROG_USE "dev8_extras_exe_"
120 LRESPR dev8_extras_blinker_bin
130 LRESPR "dev8_extras_source_outptr_bin"
140 LRESPR dev8_extras_cline_bin
150 ERT HOT_RES('z',dev8_extras_exe_make)
160 ERT HOT_REMV('z')
170 ERT HOT_RES ('z',dev8_extras_exe_linker)
180 ERT HOT_REMV('z')
190 ERT HOT_RES ('z',dev8_extras_exe_QMAC)
200 ERT HOT_REMV('z')
210 EX dev8_extras_exe_smsqemake
