* Remove string from RI stack   V0.0    1985   Tony Tebby   QJUMP
*
        section utils
*
        xdef    ut_remst        remove string (a1)
*
        include dev8_sbsext_ext_keys
*
*       d1   s  total length occupied
*       a1 c p  pointer to RI stack
*
ut_remst
        moveq   #3,d1                   round up to word, including char count
        add.w   (a6,a1.l),d1            get length
        bclr    #0,d1
        add.l   d1,bv_rip(a6)           change RIP but not a1
        rts
        end
