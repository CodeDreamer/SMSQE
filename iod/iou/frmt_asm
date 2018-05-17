; IO standard FORMAT setup   V2.02    1985  Tony Tebby   QJUMP

        section iou

        xdef    iou_frmt

        xref    iou_fdef

        include 'dev8_keys_iod'
        include 'dev8_keys_qlv'
        include 'dev8_keys_err'

;+++
; Standard format preamble.
;    Finds the old definition block
;      if found: checks if drive is in use
;        if not: removes old definition block, removes slave blocks
;        sets status to modifed.
;
;    Calls device dependent format routine.
;
;    d1 c  p drive number
;    a3 c  p pointer to linkage block
;---
iou_frmt

; find drive and check for 'in use'

        moveq   #0,d6
        move.w  d1,d6
        swap    d6                       ; standard drive number

        jsr     iou_fdef                 ; find definition
        blt.s   ifm_frmt                 ; ... none

        tst.b   iod_nrfl(a4)             ; any files open?
        bne.s   ifm_inus                 ; ... yes

; remove old definition block (cleaning up)

reglist reg     d1/a1/a3
        movem.l reglist,-(sp)            ; save call parameters
        clr.l   -(a2)                    ; remove pointer
        move.l  a4,a0
        move.w  mem.rchp,a2
        jsr     (a2)                     ; release the block
        movem.l (sp)+,reglist            ; restore parameters

; do normal format

ifm_frmt
        jmp     iod_fdrv(a3)             ; and format drive

ifm_inus
        tst.b   iod_ftyp(a4)             ; in use for direct access?
        blt.s   ifm_frmt                 ; ... yes, format anyway
        moveq   #err.fdiu,d0             ; ... no, drive in use
        rts

        end
