* Queue maintenance: set end of file   V2.00    Tony Tebby  QJUMP
*
        section ioq
*
        xdef    ioq_seof
*
        include dev8_keys_qu
*
*       a2 c  p pointer to queue 
*
*       all other registers preserved
*       no error return, condition codes N set if already end of file
*
ioq_seof
        bset    #7,qu_eoff(a2)          set end of file
        rts
        end
