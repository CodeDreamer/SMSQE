* Check number of parameters    1985   Tony Tebby  QJUMP
*
        section utils
*
        xdef    ut_parn         check d1/8 parameters
        xdef    ut_par0         check none
        xdef    ut_par1         check one
        xdef    ut_par2         check two
*
        include dev8_sbsext_ext_keys
*
ut_par2
        moveq   #$10,d0                 2 parameters
        bra.s   ut_parn
ut_par1
        moveq   #$08,d0                 1 parameter
        bra.s   ut_parn
ut_par0
        moveq   #$00,d0                 no parameters
ut_parn
        add.l   a3,d0                   number + bottom pointer
        sub.l   a5,d0                   less top
        beq.s   utp_rts                 ... should be zero
*        
        moveq   #err.bp,d0              error
        addq.l  #4,sp                   ... remove return
utp_rts
        rts
        end
