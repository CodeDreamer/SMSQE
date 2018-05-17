* Open routine for pointer routines    1986  Tony Tebby   QJUMP
*
        section driver
*
        xdef    pt_open
*
        xref    pt_wchka
        xref    pt_ptop
        xref    pt_mrall
        xref    pt_setq
        xref    pt_copyc
*
        include dev8_keys_err
        include dev8_keys_qlv
        include dev8_keys_chn
        include dev8_keys_sys
        include dev8_keys_qdos_sms
        include dev8_keys_con
*
pt_open
        move.l  sys_ckyq(a6),-(sp)       clear out key queue
        clr.l   sys_ckyq(a6)
        move.l  pt_copen-pt_liodm+pt_liod(a3),a4  get old open routine
        move.l  a3,-(sp)
        move.l  pt_clink-pt_liodm+pt_liod(a3),a3
        jsr     (a4)                    call open
        move.l  (sp)+,a3
        move.l  (sp)+,sys_ckyq(a6)      restore key queue
        tst.l   d0                      problems?
        bne.s   pto_rts                 ...oops
*
        move.l  a0,-(sp)                 save channel address
        lea     -pt_liodm+pt_liod(a3),a3 fix dddb address
        moveq   #sms.info,d0            get current job
        trap    #1
        lea     pt_head(a3),a0          point to list of primaries
chkn_lp
        move.l  (a0),d0                 next primary
        beq.s   new_job                 isn't one, there's a new job in town!
        move.l  d0,a0                   point to its link
        cmp.l   chn_ownr-sd_prwlt-sd.extnl(a0),d1
        beq.s   pto_exr                 owned by current job, should be OK
        bra.s   chkn_lp
*
new_job
        moveq   #1,d3                   new job, lock all windows
        jsr     pt_wchka(pc)
        beq.s   pto_setq                ... ok, set the queue to dummy
        bsr.l   pt_ptop                 ... bad, repick the top
        move.l  (sp)+,a0                return the heap
        move.w  mem.rchp,a2
        jsr     (a2)
        moveq   #err.imem,d0            oops
pto_rts
        rts
pto_setq
        bsr.l   pt_setq                 set keyboard queue
        bsr.l   pt_mrall                and finish off wchka
pto_exr
        moveq   #0,d0                   no errors
        move.l  (sp)+,a0
        jmp     pt_copyc(pc)            so fix up the cdb
*
        end
