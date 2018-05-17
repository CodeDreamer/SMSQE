* Check if processing (in procedure)  V0.0    1985  Tony Tebby  QJUMP
*
        section utils
*
        xdef    ut_ckprc
*
        include dev8_sbsext_ext_keys
*
ut_ckprc
        move.l  bv_rtp(a6),d0           is return stack empty?
        sub.l   bv_rtbas(a6),d0
        beq.s   ckp_rts                 ... yes, done
*
        tst.b   bv_sing(a6)             is it single line command?
        beq.s   ckp_ni                  ... no
*
        st      bv_undo(a6)             ... yes, undo and try again
        addq.l  #4,sp                   return directly
ckp_ni
        moveq   #err.ni,d0              not implemented
ckp_rts
        rts
        end
