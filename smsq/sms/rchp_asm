* Standard Return to Common Heap  V2.00    1986  Tony Tebby  QJUMP
*
        section sms
*
        xdef    sms_rchp
*
        xref    mem_rchp                return to heap
        xref    sms_rte                 return
*
        include dev8_keys_chp
*
*       d0      0 no errors detected
*       a0 c s  base address of area to return   
*
*       all other registers preserved
*
sms_rchp
        sub.w   #chp.len,a0             backspace to header
        bsr.l   mem_rchp
        bra.l   sms_rte
        end

