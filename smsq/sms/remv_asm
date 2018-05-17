* Remove items from system lists  V2.00    1986  Tony Tebby  QJUMP
*
        section sms
*
        xdef    sms_rexi
        xdef    sms_rpol
        xdef    sms_rshd
        xdef    sms_riod
        xdef    sms_rfsd
*
        xref    mem_rlst
        xref    sms_rtok
*
        include dev8_keys_sys
*
*       d0  r   return OK
*       a0 c  p address of link
*
*       all other registers preserved
*
sms_rexi
        moveq   #sys_exil,d0
        bra.s   sms_remv
sms_rpol
        moveq   #sys_poll,d0
        bra.s   sms_remv
sms_rshd
        moveq   #sys_shdl,d0
        bra.s   sms_remv
sms_riod
        moveq   #sys_iodl,d0
        bra.s   sms_remv
sms_rfsd
        moveq   #sys_fsdl,d0
*
sms_remv
        move.l  a1,d7
        lea     (a6,d0.l),a1            address of start of linked list
        bsr.l   mem_rlst                remove
        move.l  d7,a1
        bra.l   sms_rtok
        end
