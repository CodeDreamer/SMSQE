; HOTKEY DO routines   V2.01     1988   Tony Tebby   QJUMP

        section hotkey

        xdef    hk_do                   ; do hotkey

        xref    hk_llrc
        xref    hk_stfpr
        xref    hk_stfbf
        xref    hk_stuff
        xref    hk_stfcm
        xref    hk_xthg
        xref    hk_xfil
        xref    hk_pick

        include 'dev8_keys_err'
        include 'dev8_ee_hk_data'
        include 'dev8_mac_assert'

;+++
; This routine 'does' a hotkey operation. It can be called from any user code.
;
;       a1 c  p pointer to hotkey item
;       a3 c  p linkage block
;       a6 c  p bottom limit of stack (for pick/wake job)
;       error returns standard
;---
hk_do
        moveq   #$fffffffe,d0            ; ignore transient flag
        and.w   hki_type(a1),d0
        addq.w  #-hki.llrc,d0 
        cmp.w   #hki.wkxf-hki.llrc,d0    ; valid type?
        bhi.s   hkd_nimp                 ; ... no

        move.w  hkd_jump(pc,d0.w),d0
        jmp     hkd_jump(pc,d0.w) 
hkd_jump        
        dc.w    hk_llrc-hkd_jump
        dc.w    hk_stfpr-hkd_jump
        dc.w    hk_stfbf-hkd_jump
        dc.w    hk_stuff-hkd_jump
        dc.w    hk_stfcm-hkd_jump
        dc.w    hkd_code-hkd_jump
        dc.w    hk_xthg-hkd_jump
        dc.w    hk_xfil-hkd_jump
        dc.w    hk_pick-hkd_jump
        dc.w    hkd_wake-hkd_jump
        dc.w    hkd_wkxf-hkd_jump

hkd_code
        move.l  hki_ptr(a1),d0            ; anything to do?
        beq.s   hkd_rts
        move.l  d0,-(sp)                  ; ... yes, do it
hkd_rts
        rts

hkd_wake
        jsr     hk_pick                   ; wake (try pick first)
        beq.s   hkd_rts
        jmp     hk_xthg                   ; try execute thing

hkd_wkxf
        jsr     hk_pick                   ; wake or execute file - try pick
        beq.s   hkd_rts
        jmp     hk_xfil                   ; try execute file

hkd_nimp
        moveq   #err.nimp,d0
        rts
        end
