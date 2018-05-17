* Free a Thing                          v0.00   Feb 1988  J.R.Oakley  QJUMP
*
        section thing
*
        include 'dev8_mac_assert'
        include 'dev8_keys_err'
        include 'dev8_keys_sys'
        include 'dev8_keys_thg'
*
        xref    th_chkjb
        xref    th_find
        xref    th_remu
        xref    th_exit
*
        xdef    th_fthg
*+++
* Find a thing, given its name, and remove a usage block belonging to
* the given job.
*
*       Registers:
*               Entry                           Exit
*       D0                                      0, ITNF, IJOB
*       D1      Job ID or -1                    Job ID
*       D2/D3                                   smashed
*       A0      name of thing (>=3 chars)       preserved
*       A1-A3                                   smashed
*       A6      pointer to system variables     preserved
*---
th_fthg
        jsr     th_find(pc)             ; find the Thing
        bne.s   thf_exit                ; ...oops
        jsr     th_chkjb(pc)            ; does the job exist?
        bne.s   thf_exit                ; ...no
        jsr     th_remu(pc)             ; remove Job from usage list
thf_exit
        jmp     th_exit(pc)
*
        end
