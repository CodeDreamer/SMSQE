* Screen Dump Scheduler  V2.00       1987  Tony Tebby   QJUMP
*
        section sdp
*
        xdef    sdp_sched
*
        xref    sdp_psdo
        xref    sdp_retry
*
        include dev8_keys_err
        include dev8_keys_sys
        include dev8_keys_jcb
        include dev8_keys_qdos_ioa
        include dev8_keys_qdos_io
        include dev8_sys_sdp_ddlk
        include dev8_sys_sdp_data
*
jb_rela6 equ    $16
*
sdp_sched
        tst.b   sdd_go(a3)               ; go?
        beq.s   dps_rts                  ; ... no
        not.b   sdd_skip(a3)             ; skip?
        beq.s   dps_rts
*
        move.l  sys_jbpt(a6),a0          ; current job
        move.l  (a0),a0
        lea     jb_rela6(a0),a0
        move.b  (a0),-(sp)               ; relative addressing flag
        move.l  a0,-(sp)
        clr.b   (a0)

        tst.b   sdd_go(a3)               ; going?
        bgt.s   dps_go                   ; ... no
        lea     sdd_work(a3),a5          ; set work area
        move.l  a3,-(sp)                 ; save linkage
        jsr     sdp_retry                ; dump (already setup)
        move.l  (sp)+,a3
        bra.s   dps_test
dps_go
        st      sdd_go(a3)               ; set going
        clr.l   -(sp)                    ; set full screen
        clr.l   -(sp)
        move.l  sp,a1
        moveq   #0,d2
        jsr     sdp_psdo                 ; do it
        addq.l  #8,sp

dps_test
        move.l  (sp)+,a0
        move.b  (sp)+,(a0)               ; restore relative addressing flag

        addq.l  #-err.nc,d0              ; nc?
        beq.s   dps_rts                  ; ... carry on
        move.l  sdd_work+dp_chan(a3),d0  ; close channel
        beq.s   dps_done
        move.l  d0,a0
        clr.l   sdd_work+dp_chan(a3)     ; none now
        moveq   #ioa.clos,d0
        trap    #2
dps_done
        clr.l   sdd_chan(a3)             ; no channel
        clr.b   sdd_go(a3)               ; no dump
dps_rts
        rts
        end
