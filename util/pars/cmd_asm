; Identify Command           V2.00   1989  Tony Tebby

        section pars

        xdef    ps_cmd

        xref    cv_uctab
        xref    cv_cttab

        include 'dev8_keys_err'
        include 'dev8_keys_k'
        include 'dev8_mac_assert'

;+++
; Command line identify command
;
; This routine identifies a command. The commands are identified by a table
; with fixed length entries. Only the first word of each entry is used. This is
; a relative pointer to the command name (standard upper case string).
; The pointer to the definition table points to a word giving the length of
; each entry in the following table. The command line
; is a zero terminated string of characters. Leading spaces are stripped,
; then all the characters up to the next non-alpha are upper-cased.
;
; The table is terminated with a zero word.
;
;       Registers:
;               Entry                           Exit
;       D0                                      0 or error code
;       A1      pointer to command line         if found, updated to end of cmd
;       A2      pointer to definition table     points to entry for this command
;       Status return 0 or err.itnf (not recognised) err.isyn (bad syntax)
;---
ps_cmd
pcp.reg reg     d1/d2/d3/a0/a3/a4
        movem.l pcp.reg,-(sp)

        move.l  a1,a0
        lea     cv_uctab,a3
        lea     cv_cttab,a4
        moveq   #0,d1
pcp_upcas
        move.b  (a0),d1                  ; next character
        move.b  (a4,d1.w),d0             ; type
        ble.s   pcp_look                 ; ... non alpha
        assert  0,k.dig09-2,k.lclet-4,k.uclet-6
        subq.b  #k.uclet,d0
        bgt.s   pcp_look                 ; ... non alpha
        move.b  (a3,d1.w),(a0)+          ; uppercase
        bra.s   pcp_upcas

pcp_look
        move.l  a0,d2
        sub.l   a1,d2                    ; length of command
        beq.s   pcp_isyn                 ; ... bad
        move.w  (a2)+,d3                 ; entry length
        sub.w   d3,a2

pcp_loop
        add.w   d3,a2
        move.w  (a2),d0                  ; next command name
        beq.s   pcp_itnf                 ; ... oops
        move.l  a2,a3
        add.w   d0,a3
        cmp.w   (a3)+,d2                 ; the right length?
        bne.s   pcp_loop
        move.l  a1,a0
        move.w  d2,d0
        subq.w  #1,d0
pcp_ckc
        cmpm.b  (a0)+,(a3)+              ; check character
        dbne    d0,pcp_ckc
        bne.s   pcp_loop

        move.l  a0,a1                    ; return end of command
        moveq   #0,d0

pcp_exit
        movem.l (sp)+,pcp.reg
        rts
pcp_isyn
        moveq   #err.isyn,d0
        bra.s   pcp_exit
pcp_itnf
        moveq   #err.itnf,d0
        bra.s   pcp_exit
        end
