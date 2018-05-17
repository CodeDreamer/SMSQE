* Memory manager linked list maintenance   V2.00   Tony Tebby  QJUMP
*
        section mem
*
        xdef    mem_rlst        remove from list
*
        include dev8_keys_err
*
*       a0 c  p link to be removed
*       a1 c  u pointer to previous item (or start of list)
*
*       all other registers preserved
*
mem_rlst
        move.l  d0,-(sp)
        move.l  (a1),d0                 first link
        beq.s   mrl_exit                ... none
mrl_loop
        cmp.l   a0,d0                   our item?
        beq.s   mrl_unlk                ... yes, unlink
        move.l  d0,a1                   next address
        move.l  (a1),d0                 next link
        bne.s   mrl_loop                ... ok
mrl_exit
        move.l  (sp)+,d0
        rts
*
mrl_unlk
        move.l  (a0),(a1)               reset link to item removed
        move.l  (sp)+,d0
        rts
        end
