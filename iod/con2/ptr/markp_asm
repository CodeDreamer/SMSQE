* Mark window picked    1989  Tony Tebby   QJUMP
*
        section driver
*
        xdef    pt_markp
*
        include dev8_keys_con
        include dev8_keys_chn
        include dev8_keys_qdos_io
*
* a0 pointer to primary
*
* This will only be done if window waiting for IO
*
pt_markp
        move.l  a0,-(sp)
ptm_look
        tst.b   chn_stat-sd.extnl(a0)   waiting?
        bne.s   ptm_cptr                ... yes
        move.l  sd_sewll(a0),d0         ... no, try next secondary
        beq.s   ptm_done                ... no more secondaries, done
        move.l  d0,a0
        lea     -sd_sewll(a0),a0        ... NOP !!!!
        bra.s   ptm_look
*
ptm_cptr
        bset    #sd.pick,sd_pick(a0)    set picked

ptm_done
        moveq   #0,d0
        move.l  (sp)+,a0
        rts
        end

        end
