* Standard Allocate in Common Heap  V2.00    1986  Tony Tebby  QJUMP
*
        section sms
*
        xdef    sms_achp
*
        xref    sms_ckid                check job ID
        xref    mem_achp                allocate in heap
        xref    sms_rte                 return
*
        include dev8_keys_chp
*
*       d0      out of memory, invalid job
*       d1 cr   space required / space allocated
*       d2 cr   job ID
*       a0  r   base address of area allocated   
*
*       all other registers preserved
*
sms_achp
        exg     d1,d2                   check job ID
        bsr.l   sms_ckid
        exg     d1,d2
        bne.s   sac_exit                ... oops
*
        moveq   #chp.len,d7
        add.l   d7,d1                   allocate extra for header
        bsr.l   mem_achp
        bne.s   sac_exit                ... oops
        move.l  d2,chp_ownr(a0)         set owner
;**     sub.l   d7,d1                   set actual space available
        add.l   d7,a0                   and point to it
        moveq   #0,d0                   ok
sac_exit
        bra.l   sms_rte
        end
