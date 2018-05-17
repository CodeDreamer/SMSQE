;       Get primary window ID of current job

        section utility

        include dev8_keys_chn
        include dev8_keys_err
        include dev8_keys_con
        include dev8_keys_sys
        include dev8_keys_qdos_sms

        xdef    ut_gpwjb

;+++
; Get primary window ID of current job
;
;               Entry                   Exit
;       a0                              channel ID
;
;       Error returns: err.itnf         Job has no primary window
;---
ut_gpwjb
        moveq   #-1,d1                  ; myself
        moveq   #sms.info,d0
        trap    #do.sms2                ; get real job ID and system variables

        moveq   #err.itnf,d0            ; assume we'll fail
        move.l  sys_chtb(a0),a1         ; base of channel table
        moveq   #0,d2                   ; first entry
gpw_loop
        move.l  (a1)+,d3                ; pointer to channel def
        bmi.s   gpw_next                ; closed?
        move.l  d3,a0
        cmp.l   chn_ownr(a0),d1         ; are we owner?
        bne.s   gpw_next
        btst    #sd..prwn,sd.extnl+sd_prwin(a0) ; is it a primary?
        beq.s   gpw_next
        swap    d2
        move.w  chn_tag(a0),d2
        swap    d2
        move.l  d2,a0
        moveq   #0,d0
        rts

gpw_next
        addq.w  #1,d2
        cmp.w   sys_chtp(a0),d2         ; end of list?
        bls     gpw_loop
        rts
        end
