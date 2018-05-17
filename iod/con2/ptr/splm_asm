* Set pointer limits   V1.00    1986   Tony Tebby    QJUMP
*
        section driver
*
        xdef    pt_splm
*
        include dev8_keys_con
        include dev8_keys_err
*
pt_splm
        movem.l d1/d2,pt_minxy(a3)       ; set limits
        st      pt_ptlim(a3)             ; and flag
        moveq   #0,d0
        rts
        end
