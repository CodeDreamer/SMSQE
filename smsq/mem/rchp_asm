* Return space to common heap  V2.00    1986  Tony Tebby   QJUMP
*
        section mem
*
        xdef    mem_rchp
*
        xref    mem_rusb                round up in slave block allocation units
        xref    mem_rehp                'return' memory to heap
        xref    mem_rlsb                release slave blocks (a0 by d1)
*
        include dev8_keys_sys
        include dev8_keys_chp
*
*       d0  r   error return 0
*       a0 c s  base of area to be returned
*       a6 c  p system variables area base
*
*       all other registers preserved
*
reglist reg     d1/a1
*
mem_rchp
        movem.l reglist,-(sp)           save volatiles
        moveq   #chp.free,d0
        move.l  d0,chp_ownr(a0)         set owner free
        move.l  chp_flag(a0),d0         flag address
        beq.s   mrc_rehp
        move.l  d0,a1                   set flag
        st      (a1)
*
mrc_rehp
        lea     sys_chpf(a6),a1         release to common heap
        move.l  chp_len(a0),d1          get length
        bsr.s   mem_rehp                ... and release
*
        move.l  a0,a1                   base of new free space
        add.l   chp_len(a0),a0          top of new free space
*
        cmp.l   sys_fsbb(a6),a0         filing system base?
        bne.s   mrc_exok                ... no
        move.l  a1,d1                   get base of area
        bsr.l   mem_rusb                and round up
        move.l  a0,d0                   top, less rounded up base 
        sub.l   d1,d0                   is amount to release to fsb
        beq.s   mrc_exok                ... none
        move.l  d1,a0                   base of bit to release
        move.l  d0,d1                   and amount
        sub.l   d1,chp_len(a1)          remains of new free space
        bne.s   mrc_rel                 ... there is some
*
        lea     sys_chpf-chp_nxfr(a6),a1 ... none, so remove it from list
*
mrc_look
        move.l  chp_nxfr(a1),d0         someone must point to it
        beq.s   mrc_exok                ... but they don't
        add.l   a1,d0
        cmp.l   d0,a0                   does this one?
        beq.s   mrc_rem                 ... it does, remove it
        move.l  d0,a1
        bra.s   mrc_look
*
mrc_rem
        clr.l   chp_nxfr(a1)            remove link
*
mrc_rel
        bsr.l   mem_rlsb                release slave blocks (a0 by d1)
        move.l  a0,sys_fsbb(a6)         reset filing system slave block base
mrc_exok
        moveq   #0,d0
        movem.l (sp)+,reglist
        rts
        end
