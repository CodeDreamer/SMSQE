; Command line parameters           V2.00   1989  Tony Tebby

        section pars

        xdef    ps_clprm

        xref    cv_upcas

        include 'dev8_keys_err'

mek.key setstr  {\}
mek.key equ     '[mek.key]'

pcpt_opt equ    $00     ; char  parameter options
pcpt_key equ    $01     ; char  parameter key (uc)
pcpt_flg equ    $02     ; word  pointer (a5) to flag
pcpt_prm equ    $04     ; word  pointer (a5) to param
pcpt_len equ    $06     ; word  max length of parameter string (dual)
pcpt.len equ    $08

pcp_sksp
        cmp.b   (a1)+,d3                 ; skip to non-space
        beq.s   pcp_sksp
        move.b  -1(a1),d1                ; set next char
        rts

;+++
; Command line parameter interpreter.
;
; This routine interprets the parameters of a 'command line'. The line is
; passed as a standard string, and is processed with the aid of a table of
; definitions:
;
;       word '  '  position dependent parameter (can follow key)
;            '+ '  position dependent parameter required
;            ' x'  parameter keyed with x (lower case) (optional string)
;            '+x'  parameter keyed with x (compulsory string)
;            '-x'  parameter keyed with x (flag only)
;
;       word       pointer to flag set if parameter found (rel a5)
;
;       word       pointer to buffer for parameter string (rel a5)
;
;       word       max length of parameter string (first byte ' ' or '+')
;
; The table is terminated with a zero word.
;
; The key character is '\'
; For each parameter found, the flag is set. Strings containing spaces or the
; key character must be enclosed in braces ({...}). The string is transferred
; as a standard string (length in a word) followed by a zero.
;
;
;       Registers:
;               Entry                           Exit
;       D0                                      0 or error code
;       A1      pointer to command line         updated to error char
;       A2      pointer to definition table
;       A5      pointer to results area
;       Status return standard
;---
ps_clprm
pcp.reg reg     d1/d2/d3/d4/a0/a2/a3
stk_tabl equ    $14
        movem.l pcp.reg,-(sp)
        moveq   #' ',d3                  ; space is useful
        moveq   #mek.key,d4              ; ... so is the key

pcp_loop
        bsr.s   pcp_sksp                 ; next parameter
        beq.l   pcp_ckend
        cmp.b   d4,d1                    ; key?
        bne.s   pcp_posd

pcp_key
        move.b  (a1)+,d1                 ; next key
        beq.l   pcp_ipar
        jsr     cv_upcas                 ; upper case it
        move.l  stk_tabl(sp),a2          ; reset table pointer
        subq.l  #pcpt.len,a2
pcp_look
        addq.l  #pcpt.len,a2
        move.w  (a2),d0                  ; next option / key
        beq.l   pcp_ipar                 ; ... oops
        cmp.b   d0,d1                    ; this one?
        bne.s   pcp_look

        move.w  pcpt_flg(a2),a0          ; key found
        st      (a5,a0.l)                ; ... set flag

        cmp.b   #'-',pcpt_opt(a2)        ; string to follow?
        beq.s   pcp_loop                 ; ... no

        bsr.s   pcp_sksp                 ; next non space
        beq.s   pcp_ckprq                ; ... none
        cmp.b   d4,d1                    ; key?
        bne.s   pcp_parm                 ; ... no, good
pcp_ckprq
        cmp.b   #'+',pcpt_opt(a2)        ; param required?
        beq.s   pcp_ipar                 ; ... yes
        tst.b   d1                       ; end?
        bne.s   pcp_key                  ; ... no, look at next key
        bra.s   pcp_exok                 ; ... yes, done

pcp_posd
        cmp.b   pcpt_key(a2),d3          ; position dependent parameter next?
        bne.s   pcp_ipar                 ; ... no
        move.w  pcpt_flg(a2),a0          ; ... yes,
        st      (a5,a0.l)                ; ... set flag

pcp_parm
        move.w  pcpt_len(a2),d0          ; max length
        move.w  pcpt_prm(a2),a0
        add.l   a5,a0                    ; parameter here
        clr.w   (a0)+                    ; zero the length
        move.l  a0,a3                    ; keep the start

        moveq   #1,d2                    ; brace level
        cmp.b   #'{',d1                  ; braces?
        beq.s   pcp_braces               ; ... yes
        subq.w  #1,d0                    ; ... no, we already have one character

pcp_cstr
        move.b  d1,(a0)+
        move.b  (a1)+,d1                 ; next character
        cmp.b   d3,d1                    ; space?
        bls.s   pcp_ends                 ; ... or end of command
        cmp.b   d4,d1                    ; next key?
        beq.s   pcp_ends
        dbra    d0,pcp_cstr
        bra.s   pcp_ipar                 ; too many

pcp_brloop
        move.b  d1,(a0)+                 ; copy character within braces
pcp_braces
        move.b  (a1)+,d1                 ; next character
        beq.s   pcp_ipar                 ; ... oops
        cmp.b   #'}',d1                  ; end brace?
        bne.s   pcp_obr                  ; ... no
        subq.w  #1,d2                    ; ... yes, last one?
        beq.s   pcp_endp                 ; ... yes
pcp_obr
        cmp.b   #'{',d1                  ; another level?
        bne.s   pcp_brend
        addq.w  #1,d2                    ; ... yes
pcp_brend
        dbra    d0,pcp_brloop
        bra.s   pcp_ipar                 ; ... oops, too long

pcp_ends
        subq.l  #1,a1                    ; backspace
pcp_endp
        clr.b   (a0)                     ; null at end
        sub.l   a3,a0                    ; length
        move.w  a0,-(a3)                 ; ... at start
        addq.l  #pcpt.len,a2             ; next param (position dep?)
        bra     pcp_loop

pcp_ckend
        cmp.w   #'+ ',(a2)               ; parameter required?
        beq.s   pcp_ipar                 ; ... yes

pcp_exok
        moveq   #0,d0                    ; set error code

pcp_exit
        movem.l (sp)+,pcp.reg
        rts
pcp_ipar
        subq.l  #1,a1
        moveq   #err.ipar,d0
        bra.s   pcp_exit

        end
