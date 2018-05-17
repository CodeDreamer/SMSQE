; DV3 Locate Format Dependent Vector       V3.00           1992 Tony Tebby

        section dv3

        xdef    dv3_locfd

        xref    dv3_setfd

        include 'dev8_dv3_keys'
        include 'dev8_keys_err'

;+++
; This is the routine that is used to scan the table of format dependent vectors
; to try and find a vector with a particular type.
;
;       d0 c  r format type required / error code
;       d1-d6 p
;       d7 c  p drive number
;       a0 c  p pointer to channel block
;       a1 c  p pointer to root sector
;       a2    p
;       a3 c  p pointer to linkage block
;       a4 c  u pointer to definition block
;       a5    p
;       a6 c  p pointer to system variables
;
;       Status return 0 or err.fdnf
;---
dv3_locfd
dlf.reg reg     d1/d6/a2/a5
        movem.l dlf.reg,-(sp)

        move.l  ddl_dv3(a3),a5           ; dv3 linkage
        move.w  dv3_fdmax(a5),d6         ; max format dependent
        move.l  dv3_fdtab(a5),a5         ; and list
        move.l  a5,d1                    ; any?
        beq.s   dlf_bad                  ; ... no

dlf_loop
        move.l  (a5)+,d1                 ; a format?
        beq.s   dlf_eloop                ; ... no
        move.l  d1,a2
        cmp.b   fdv_ftype(a2),d0
        beq.s   dlf_setfd
dlf_eloop
        subq.w  #1,d6                    ; next format?
        bge.s   dlf_loop

dlf_bad
        moveq   #err.fdnf,d0             ; not recognised
        bra.s   dlf_exit

dlf_setfd
        jsr     dv3_setfd

dlf_exit
        movem.l (sp)+,dlf.reg
        rts

        end
