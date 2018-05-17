; check file name   V2.01    1986  Tony Tebby   QJUMP

        section iou

        xdef    iou_cknm
        xdef    iou_ckdn
        xdef    iou_ckfn
        xdef    iou_ckch

        xref    cv_lctab

        include 'dev8_keys_chn'
        include 'dev8_keys_err'
;+++
; Check d0 characters (a0) vs (a4) 
;
;       d0 c s  character count
;       d1   s  character
;       d2   s  lowercased character
;       a0 c s  pointer to name 1
;       a4 c s  pointer to name 2
;
;       condition codes Z if match
;---
iou_ckch
        moveq   #0,d1                    ; d1 zero word
        bra.s   icn_lset

;+++
; Check filename against directory name in channel block
;
;       d0   s  character count
;       d1   s  character
;       d2   s  lowercased character
;       a0 c s  pointer to channel block
;       a4 c s  pointer to name
;
;       d0 and condition codes Z if match
;---
iou_ckfn
        lea     chn_name(a0),a0          ; point to name
        moveq   #0,d1
        move.w  (a0)+,d0                 ; directory name
        cmp.w   (a4)+,d0                 ; file name or longer?
        bge.s   icn_rtnf                 ; ... no
        cmp.b   #'_',(a4,d0.w)           ; ... longer, there must be an underscr
        beq.s   icn_lset                 ; ... ... yes
icn_rtnf
        moveq   #err.fdnf,d0
        bra.s   icn_rts                  ; ... ... no

;+++
; Check name against filename in channel block
;
;       d0   s  character count
;       d1   s  character
;       d2   s  lowercased character
;       a0 c s  pointer to channel block
;       a4 c s  pointer to name
;+++
; Check directory name against filename in channel block
;
;       d0   s  character count
;       d1   s  character
;       d2   s  lowercased character
;       a0 c s  pointer to channel block
;       a4 c s  pointer to name
;
;       d0 and condition codes Z if match
;---
iou_ckdn
        lea     chn_name(a0),a0          ; point to name
        moveq   #0,d1
        move.w  (a4)+,d0                 ; directory name
        cmp.w   (a0)+,d0                 ; the same or longer?
        bgt.s   icn_rts                  ; ... no
        beq.s   icn_lset                 ; ... the same
        cmp.b   #'_',(a0,d0.w)           ; ... longer, there must be an underscr
        beq.s   icn_lset                 ; ... ... yes
        bne.s   icn_rts                  ; ... ... no

;+++
; Check name against filename in channel block
;
;       d0   s  character count
;       d1   s  character
;       d2   s  lowercased character
;       a0 c s  pointer to channel block
;       a4 c s  pointer to name
;
;       d0 and condition codes Z if match
;---
iou_cknm
        lea     chn_name(a0),a0          ; point to name
        moveq   #0,d1
        move.w  (a0)+,d0                 ; name length
        cmp.w   (a4)+,d0                 ; the same?
        bne.s   icn_rts                  ; ... no

icn_lset
        subq.w  #1,d0                    ; any characters?
        blt.s   icn_ok
        move.l  a1,-(sp)
        lea     cv_lctab,a1
icn_loop
        move.b  (a4)+,d1
        move.b  (a1,d1.w),d2             ; first character
        move.b  (a0)+,d1
        sub.b   (a1,d1.w),d2             ; same as second?
icn_lend
        dbne    d0,icn_loop
        move.l  (sp)+,a1
        bne.s   icn_rts
icn_ok
        moveq   #0,d0
icn_rts
        rts
        end

