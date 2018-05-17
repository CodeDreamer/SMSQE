* Respr handling       V0.12   1985  Tony Tebby   QJUMP
*
*       RESPR (nbytes)                  allocate in res proc area
*
        section exten
*
        xdef    respr
*
        xref    ext_flint
        xref    ut_rtfd1
*
        include dev8_sbsext_ext_keys
*
* allocate in res proc area
*
respr
        bsr.s   ext_flint               get one long integer argument
        bne.s   resp_exit               ... oops

        moveq   #mt.alres,d0            allocate resident procedure area
        move.l  d1,d4                   save length
        trap    #1
        tst.l   d0
        beq.s   resps                   ... ok
        move.l  d4,d1
        moveq   #mt.alchp,d0            try heap
        moveq   #-1,d2                  allocate permanent
        trap    #1
        tst.l   d0
        bne.s   resp_exit               ... oops

resps
        move.l  a0,d1                   return the base address
        bra.l   ut_rtfd1
resp_exit
        rts
        end
