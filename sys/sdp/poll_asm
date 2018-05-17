* Screen Dump polling  V2.00       1987  Tony Tebby   QJUMP
*
        section sdp
*
        xdef    sdp_poll
*
        include dev8_keys_sys
        include dev8_keys_qu
        include dev8_keys_qlv
        include dev8_sys_sdp_ddlk
*
*
sdp_poll
        move.l  sys_ckyq(a6),a2         set keyboard queue pointer
        move.l  a3,a4                   keep linkage block address safe
*
        move.w  ioq.test,a3             test the queue
        jsr     (a3)
        bne.s   sdp_rts                 ... nothing there
        addq.b  #1,d1                   ... something, is it $FF
        bne.s   sdp_rts                 ... ... no
*
* found ALT
*
        move.l  qu_nexto(a2),a1         now check the next character
        addq.l  #1,a1
        cmp.l   qu_endq(a2),a1          off end?
        blt.s   sdp_tempty              ... no, test empty
        lea     qu_strtq(a2),a1         ... yes, reset out pointer
sdp_tempty
        move.l  qu_nexti(a2),d2         keep nextin pointer
        cmp.l   d2,a1                   in and out the same?
        beq.s   sdp_rts                 ... yes, can't read next character
        moveq   #0,d1
        move.b  (a1),d1                 get next character
        cmp.b   sdd_akey(a4),d1         screen dump character?
        bne.s   sdp_rts                 ... no
        move.l  qu_nexto(a2),qu_nexti(a2) remove all pending
        tst.b   sdd_go(a4)              going already?
        bne.s   sdp_rts                 ... yes
        move.b  #1,sdd_go(a4)           ... no, go
sdp_rts
        rts
        end
