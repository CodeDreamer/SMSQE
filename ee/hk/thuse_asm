; HOTKEY use routine  V2.00     1988   Tony Tebby   QJUMP

        section hotkey

        xdef    hk_thuse

        xref    hk_ckjob

        include 'dev8_keys_qlv'
        include 'dev8_keys_err'
        include 'dev8_ee_hk_data'

; check if job still exists

hku_ckjb
        cmp.b   #-1,hkd_act+hkd.thg(a1)  ; used by hotkey job?
        bne.s   hku_inus                 ; ... no, in use
        move.l  a3,-(sp)                 ; save linkage
        lea     hkd.thg(a1),a3           ; set the thing itself
        jsr     hk_ckjob                 ; check job still exists
        move.l  (sp)+,a3
        beq.s   hku_inus                 ; it is alright!

;+++
; Use routine for Hotkey Thing. Prevents multiple access including polling
; routine and Hotkey Job.
;---
hk_thuse
        tas     hkd_act+hkd.thg(a1)      ; kill polling routine
        bne.s   hku_ckjb                 ; ... in use, check job
        tst.b   hkd_req+hkd.thg(a1)      ; request pending?
        bne.s   hku_reqp                 ; ... yes

        moveq   #$18,d1                  ; allocate usage
        move.w  mem.achp,a2
        jmp     (a2)

hku_reqp
        clr.b   hkd_act+hkd.thg(a1)      ; re-enable polling
hku_inus
        moveq   #err.fdiu,d0
        rts
        end
